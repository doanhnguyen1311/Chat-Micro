import React from "react";
import styles from "./index.module.css";

interface Member {
    name: string;
    avatar: string;
    lastActive: string;
}

const members: Member[] = [
{
    name: "Joseph",
    avatar: "https://randomuser.me/api/portraits/women/44.jpg",
    lastActive: "13 minutes ago",
},
{
    name: "Sephiroth",
    avatar: "https://randomuser.me/api/portraits/men/30.jpg",
    lastActive: "24 days ago",
},
{
    name: "Linda Lohan",
    avatar: "https://randomuser.me/api/portraits/women/41.jpg",
    lastActive: "a year ago",
},
{
    name: "Irina Petrova",
    avatar: "https://randomuser.me/api/portraits/women/24.jpg",
    lastActive: "a year ago",
},
{
    name: "Jennie Ferguson",
    avatar: "https://randomuser.me/api/portraits/women/12.jpg",
    lastActive: "3 years ago",
},
{
    name: "Robert Cook",
    avatar: "https://randomuser.me/api/portraits/men/10.jpg",
    lastActive: "3 years ago",
},
{
    name: "Sophia Lee",
    avatar: "https://randomuser.me/api/portraits/women/22.jpg",
    lastActive: "3 years ago",
},
{
    name: "Alexis Clark",
    avatar: "https://randomuser.me/api/portraits/women/23.jpg",
    lastActive: "3 years ago",
},
{
    name: "Anna Young",
    avatar: "https://randomuser.me/api/portraits/women/24.jpg",
    lastActive: "3 years ago",
},
];

const RightSidebar: React.FC = () => {
    return (
        <div className={`${styles.container} col-lg-4`}>
            <div className={styles.card}>
                <h3 className={styles.title}>Members</h3>
                <div className={styles.tabs}>
                    <span className={styles.active}>Newest</span>
                    <span>Active</span>
                    <span>Popular</span>
                </div>
    
                <ul className={styles.memberList}>
                    {members.map((member, index) => (
                        <li key={index} className={styles.memberItem}>
                            <img
                                src={member.avatar}
                                alt={member.name}
                                className={styles.avatar}
                            />
                            <div className="d-flex flex-column gap-8px">
                                <p className={styles.name}>{member.name}</p>
                                <p className={styles.time}>{member.lastActive}</p>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default RightSidebar;
