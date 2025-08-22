import React from 'react';
import styles from '../index.module.css';
import { useNavigate } from 'react-router-dom';

export interface GroupMember {
  id: number;
  avatar: string;
  name: string;
}

export interface Group {
  id: number;
  name: string;
  coverImage: string;
  icon: string;
  activeStatus: string;
  members: GroupMember[];
  memberCount: number;
  privacy: 'Public Group' | 'Private Group';
  joined: boolean;
}

export interface GroupCardProps {
  group: Group;
  onJoinGroup: (groupId: number) => void;
}

const GroupCard: React.FC<GroupCardProps> = ({ group, onJoinGroup }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/group/${group.id}/group-home`);
  };

  return (
    <div onClick={handleClick} className={styles.groupCard}>
      {/* Cover Image */}
      <div className={styles.coverImageContainer}>
        <img 
          src={group.coverImage} 
          alt={group.name}
          className={styles.coverImage}
        />
        {/* Group Icon */}
        <div className={styles.groupIconContainer}>
          <img 
            src={group.icon} 
            alt={group.name}
            className={styles.groupIcon}
          />
        </div>
      </div>
      
      {/* Group Info */}
      <div className={styles.groupInfo}>
        <h3 className={styles.groupName}>{group.name}</h3>
        <p className={styles.activeStatus}>{group.activeStatus}</p>
        
        {/* Members Avatars */}
        <div className={styles.membersContainer}>
          <div className={styles.memberAvatars}>
            {group.members.slice(0, 5).map((member, index) => (
              <img 
                key={member.id}
                src={member.avatar} 
                alt={member.name}
                className={styles.memberAvatar}
                style={{ zIndex: 5 - index }}
              />
            ))}
          </div>
        </div>
        
        {/* Group Stats */}
        <p className={styles.groupStats}>
          {group.privacy} / {group.memberCount} members
        </p>
        
        {/* Join Button */}
        <button 
          onClick={() => onJoinGroup(group.id)}
          className={styles.joinButton}
        >
          Join Group
        </button>
      </div>
    </div>
  );
};

export default GroupCard;