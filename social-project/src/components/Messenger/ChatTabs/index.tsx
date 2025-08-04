import React from "react";
import styles from "../index.module.css";

interface ChatTabsProps {
  currentTab: "friends" | "groups";
  onChange: (tab: "friends" | "groups") => void;
}

const ChatTabs: React.FC<ChatTabsProps> = ({ currentTab, onChange }) => {
  return (
    <div className={styles.tabs}>
      <span
        className={`${styles.tab} ${currentTab === "friends" ? styles.active : ""}`}
        onClick={() => onChange("friends")}
      >
        Friends
      </span>
      <span
        className={`${styles.tab} ${currentTab === "groups" ? styles.active : ""}`}
        onClick={() => onChange("groups")}
      >
        Groups
      </span>
    </div>
  );
};

export default ChatTabs;
