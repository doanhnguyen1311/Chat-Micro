import React, { useState } from "react";
import { UserRound, Play, UsersRound, MonitorDot } from "lucide-react";
import { useNavigate } from "react-router-dom";
import logo from '../../assets/imgs/logo.png';
import styles from "./index.module.css";

interface SidebarItem {
    label: string;
    icon: React.ReactElement;
    path: string;
}

interface SidebarProps {
    className?: string;
    onNavigate?: (path: string) => void;
}

const sidebarItems: SidebarItem[] = [
    { label: "Feeds", icon: <MonitorDot size={20}/>, path: "/feeds" },
    { label: "Watch", icon: <Play size={20}/>, path: "/watch" },
    { label: "People", icon: <UserRound size={20}/>, path: "/people" },
    { label: "Groups", icon: <UsersRound size={20}/>, path: "/groups" },
];

const MiniSidebar: React.FC<SidebarProps> = ({ className, onNavigate }) => {
    const navigate = useNavigate();
    const [activeItem, setActiveItem] = useState<string>("Feeds");

    const handleItemClick = (item: SidebarItem): void => {
        setActiveItem(item.label);
        navigate(item.path);
        
        if (onNavigate) {
            onNavigate(item.path);
        }
    };

    return (
        <div className={`${styles.sidebar} ${className || ''}`}>
            <div className={styles.header}>
                <div className={styles.logo}>
                    <img src={logo} className={styles.circle} alt="DeliSocial Logo" />
                </div>
            </div>
            <div className={styles.menu}>
                {sidebarItems.map((item: SidebarItem, index: number) => (
                    <div
                        key={index}
                        className={`${styles.menuItem} ${
                            activeItem === item.label ? styles.active : ""
                        }`}
                        onClick={() => handleItemClick(item)}
                        role="button"
                        tabIndex={0}
                        onKeyDown={(e: React.KeyboardEvent) => {
                        if (e.key === 'Enter' || e.key === ' ') {
                            handleItemClick(item);
                        }
                        }}
                    >
                        {item.icon}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MiniSidebar;