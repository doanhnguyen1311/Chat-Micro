import React from "react";
import styles from "../index.module.css";
import ava from '../../../assets/imgs/avagroup1.jpg';
import type { ChatGroup } from "../types";

interface GroupListProps {
  groups: ChatGroup[];
  onClick: (group: ChatGroup) => void;
}

const GroupList: React.FC<GroupListProps> = ({ groups, onClick }) => {
  return (
    <>
      {groups.map((group) => (
        <div key={group.id} className={styles.groupItem} onClick={() => onClick(group)}>
          <img src={ava} alt={group.name} className={styles.avatar} />
          <span className={styles.groupName}>{group.name}</span>
        </div>
      ))}
    </>
  );
};

export default GroupList;
