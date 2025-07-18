import React, { useState } from "react";
import { MessageCircle, Search, ChevronRight, Ellipsis } from "lucide-react";
import styles from "./index.module.css";

const MessengerSidebar: React.FC = () => {
  const [tab, setTab] = useState<"friends" | "groups">("friends");
  const [collapsed, setCollapsed] = useState(true);

  return (
    <div className={`${styles.sidebar} ${collapsed ? styles.collapsed : ""}`}>
      {/* Header */}
      {!collapsed && (
        <div className={styles.header}>
          <div className={styles.headerLeft}>
            <MessageCircle className={styles.icon} size={20} />
            <span className={styles.title}>DeliChat</span>
          </div>
          <Ellipsis className={styles.icon} size={20} />
        </div>
      )}

      {/* Tabs */}
      {!collapsed && (
        <div className={styles.tabs}>
          <span
            className={`${styles.tab} ${tab === "friends" ? styles.active : ""}`}
            onClick={() => setTab("friends")}
          >
            Friends
          </span>
          <span
            className={`${styles.tab} ${tab === "groups" ? styles.active : ""}`}
            onClick={() => setTab("groups")}
          >
            Groups
          </span>
        </div>
      )}

      {/* Search */}
      {!collapsed && (
        <div className={styles.searchWrapper}>
          <Search className={styles.searchIcon} />
          <input
            type="text"
            className={styles.searchInput}
            placeholder="Find friends"
          />
        </div>
      )}

      {/* Content */}
      <div className={styles.content}>
        {!collapsed ? (
          <p className={styles.noFriend}>No friend found</p>
        ) : (
          <div className={styles.iconCollapse}>
            <MessageCircle className={styles.icon} size={20} />
          </div>
        )}
      </div>

      {/* Footer */}
      <div className={styles.footer}>
        <button
          className={styles.collapseButton}
          onClick={() => setCollapsed(!collapsed)}
        >
          <ChevronRight
            size={16}
            style={{
              transform: collapsed ? "rotate(180deg)" : "rotate(0deg)",
              transition: "transform 0.5s",
            }}
          />
        </button>
        {!collapsed && <span className={styles.collapseText}>Collapse</span>}
      </div>
    </div>

  );
};

export default MessengerSidebar;
