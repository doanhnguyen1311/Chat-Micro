import type React from "react";
import { Search, UserRoundPlus, BellRing } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { LogoutAPI } from "../../api";
import { useAuth } from "../../hooks/useAuth";
import styles from "./index.module.css";

const Header: React.FC = () => {
    const navigate = useNavigate();
    const { user } = useAuth();
    const [ showFriendRequest, setShowFriendRequest ] = useState(false);
    const [ showNotifications, setShowNotifications ] = useState(false);
    const [ showUserActions, setShowUserActions ] = useState(false);
    const friendRef = useRef<HTMLDivElement>(null);
    const notiRef = useRef<HTMLDivElement>(null);
    const userMenuRef = useRef<HTMLDivElement>(null);

    const userMenuItems = [
        { label: "Profile", path: "/my-profile" },
        { label: "Friends", path: "/my-friends" },
        { label: "Groups", path: "/my-groups" },
        { label: "Notifications", path: "/notifications" },
        { label: "Messages", path: "/my-messages" },
        { label: "Settings", path: "/settings" },
        { label: "Log Out", path: "/logout" },
    ];

    useEffect(() => {
        function handleClickOutside(event: MouseEvent) {
            if (
                friendRef.current &&
                !friendRef.current.contains(event.target as Node)
            ) {
                setShowFriendRequest(false);
            }

            if (
                notiRef.current &&
                !notiRef.current.contains(event.target as Node)
            ) {
                setShowNotifications(false);
            }

            if (
                userMenuRef.current &&
                !userMenuRef.current.contains(event.target as Node)
            ) {
                setShowUserActions(false);
            }
        }
    
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    const handleNavigate = async (path: string) => {
        setShowUserActions(false);

        if (path === "/logout") {
            try {
                // Gọi API logout
                const response = await fetch(LogoutAPI, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                });

                const result = await response.json();
    
                if (result.success) {
                    // Chuyển về trang login
                    navigate("/");
                } else {
                    console.error("Logout failed");
                }
            } catch (error) {
                console.error("Error logging out:", error);
            }
        } else {
            navigate(path);
        }
    };

    return (
        <div className={`${styles.header}`}>
            {/* Search */}
            <div className={`${styles.search}`}>
                <Search size={16} />
                <input type="text" placeholder="Search..." />
            </div>

            {/* Action*/}
            <div className={`${styles.action}`}>
                <div className={`${styles.container}`} ref={friendRef}>
                    <div 
                        className={`${styles.icon}`}
                        onClick={() => setShowFriendRequest((prev) => !prev)}
                    >
                        <UserRoundPlus size={16} />
                    </div>
    
                    {showFriendRequest && (
                        <div className={styles.dropdown}>
                            <div className={styles.dropdownTitle}>Friend requests</div>
                            <div className={styles.emptyBox}>
                                <p>No friend request.</p>
                            </div>
                            <button className={styles.allRequestsButton}>All Requests</button>
                        </div>
                    )}
                </div>

                <div className={`${styles.container}`} ref={notiRef}>
                    <div 
                        className={`${styles.icon}`}
                        onClick={() => setShowNotifications((prev) => !prev)}
                    >
                        <BellRing size={16} />
                    </div>

                    {showNotifications && (
                        <div className={styles.dropdown}>
                            <div className={styles.dropdownTitle}>Notifications</div>
                            <div className={styles.emptyBox}>
                                <p>No notifications found.</p>
                            </div>
                            <button className={styles.allRequestsButton}>All Notifications</button>
                        </div>
                    )}
                </div>

                <div className={`${styles.container}`} ref={userMenuRef}>
                    <div 
                        className={`${styles.user}`}
                        onClick={() => setShowUserActions((prev) => !prev)}
                    >
                        <img
                            src={user?.profile.avatarUrl}
                            alt="User"
                            className={styles.avatar}
                        />
                        <p className="fs-14">@{user?.username}</p>
                    </div>

                    {showUserActions && (
                        <div className={styles.userDropdown}>
                            <ul>
                                {userMenuItems.map((item) => (
                                    <li
                                        key={item.path}
                                        className={styles.userDropdownItem}
                                        onClick={() => handleNavigate(item.path)}
                                    >
                                        {item.label}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}

export default Header;