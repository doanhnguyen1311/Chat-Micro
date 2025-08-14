import React, { useState } from "react";
import { UserRound, Play, UsersRound, MonitorDot } from "lucide-react";
import { useNavigate } from "react-router-dom";
import logo from '../../assets/imgs/logo.png';
import { useAuth } from "../../hooks/useAuth";
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
  { label: "Feeds", icon: <MonitorDot />, path: "/feeds" },
  { label: "Watch", icon: <Play />, path: "/watch" },
  { label: "People", icon: <UserRound />, path: "/people" },
  { label: "Groups", icon: <UsersRound />, path: "/groups" },
];

const Sidebar: React.FC<SidebarProps> = ({ className, onNavigate }) => {
  const navigate = useNavigate();
  const { user } = useAuth();
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
          <h1>DeliSocial</h1>
          <p className="fs-12">Social Network</p>
        </div>
        <div className={styles.profileCard}>
          <img
            src={user?.profile.avatarUrl}
            alt="User"
            className={styles.avatar}
          />
          <h2>{user?.profile.fullName}</h2>
          <p className={`${styles.role} fs-12 mt-8`}>@{user?.username}</p>
          <div className={styles.stats}>
            <div>
              <strong className="text-color fs-16">0</strong>
              <p className="fs-13">Friends</p>
            </div>
            <div>
              <strong className="text-color fs-16">3</strong>
              <p className="fs-13">Groups</p>
            </div>
          </div>
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
            <span>{item.label}</span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Sidebar;