import React, { useState } from "react";
import { MessageCircle, ChevronRight, Ellipsis } from "lucide-react";
import styles from "./index.module.css";
import ChatTabs from "./ChatTabs";
import SearchInput from "./SearchInput";
import GroupList from "./GroupList";
import ChatBox from "./ChatBox";
import type { ChatGroup } from "./types";

const groups: ChatGroup[] = [
  {
    id: 1,
    name: "Gamers",
    avatar: "/avatars/gamers.jpg",
    lastMessages: [
      { content: "Привет, как дела?", time: "Jul 20, 2025, 5:53 PM" },
      { content: "Роотт", time: "Jul 20, 2025, 6:59 PM" },
      { content: "Пппп", time: "Jul 20, 2025, 6:59 PM" },
      { content: "😕🥺🥶🥶", time: "Jul 20, 2025, 6:59 PM" },
    ],
  },
  {
    id: 2,
    name: "Cycling Club",
    avatar: "/avatars/cycling.jpg",
    lastMessages: [],
  },
  {
    id: 3,
    name: "Backpakers Club",
    avatar: "/avatars/backpack.jpg",
    lastMessages: [],
  },
];

const MessengerSidebar: React.FC = () => {
  const [tab, setTab] = useState<"friends" | "groups">("groups");
  
  const [collapsed, setCollapsed] = useState(true);

  const [activeChats, setActiveChats] = useState<ChatGroup[]>([]);

  const openChat = (group: ChatGroup) => {
    if (!activeChats.some((chat) => chat.id === group.id)) {
      setActiveChats((prev) => [...prev, group]);
    }
  };

  const closeChat = (id: number) => {
    setActiveChats((prev) => prev.filter((chat) => chat.id !== id));
  };

  return (
    <>
      <div className={`${styles.sidebar} ${collapsed ? styles.collapsed : ""}`}>
        {!collapsed && (
          <>
            <div className={styles.header}>
              <div className={styles.headerLeft}>
                <MessageCircle className={styles.icon} size={20} />
                <span className={styles.title}>Messenger</span>
              </div>
              <Ellipsis className={styles.icon} size={20} />
            </div>

            <ChatTabs currentTab={tab} onChange={setTab} />
            <SearchInput placeholder={`Find ${tab}`} />
            <div className={styles.content}>
              <GroupList groups={groups} onClick={openChat} />
            </div>
          </>
        )}

        {/* Collapsed icon */}
        {collapsed && (
          <div className={styles.iconCollapse}>
            <MessageCircle className={styles.icon} size={20} />
          </div>
        )}

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

      {/* Render multiple chat boxes */}
      {activeChats.map((group, index) => (
        <ChatBox key={group.id} group={group} index={index} onClose={closeChat} />
      ))}
    </>
  );
};

export default MessengerSidebar;
