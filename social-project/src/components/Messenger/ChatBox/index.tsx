import React from "react";
import { X } from "lucide-react";
import styles from "../index.module.css";
import type { ChatGroup } from "../types";
import ava from '../../../assets/imgs/avagroup1.jpg';

interface ChatBoxProps {
  group: ChatGroup;
  index: number;
  onClose: (id: number) => void;
}

const ChatBox: React.FC<ChatBoxProps> = ({ group, index, onClose }) => {
  return (
    <div className={styles.chatBox} style={{ right: `${100 + index * 340}px` }}>
      <div className={styles.chatHeader}>
        <div className={styles.chatUser}>
          <img src={ava} alt={group.name} className={styles.avatar} />
          <div>
            <div className={styles.chatName}>{group.name}</div>
            <div className={styles.chatStatus}>Offline</div>
          </div>
        </div>
        <X className={styles.closeIcon} size={18} onClick={() => onClose(group.id)} />
      </div>

      <div className={styles.chatMessages}>
        {group.lastMessages.map((msg, i) => (
          <div key={i} className={styles.messageWrapper}>
            {(i === 0 || group.lastMessages[i - 1].time !== msg.time) && (
              <div className={styles.timestamp}>{msg.time}</div>
            )}
            <div className={styles.message}>{msg.content}</div>
          </div>
        ))}
      </div>

      <div className={styles.chatInput}>
        <input type="text" placeholder="Write your message" />
        <span className={styles.emoji}>😊</span>
      </div>
    </div>
  );
};

export default ChatBox;
