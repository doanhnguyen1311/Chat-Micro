import React from 'react';
import styles from '../index.module.css';

export interface User {
  id: number;
  name: string;
  avatar: string;
  activeStatus: string;
  friends: number;
  groups: number;
  actionType: 'profile' | 'cancel';
  actionText: string;
}

export interface PeopleCardProps {
  user: User;
  onAction: (userId: number, actionType: string) => void;
}

const PeopleCard: React.FC<PeopleCardProps> = ({ user, onAction }) => {
  return (
    <div className={styles.peopleCard}>
      {/* Avatar */}
      <div className={styles.avatarContainer}>
        <img 
          src={user.avatar} 
          alt={user.name}
          className={styles.avatar}
        />
      </div>
      
      {/* Name */}
      <h3 className={styles.userName}>{user.name}</h3>
      
      {/* Active Status */}
      <p className={styles.activeStatus}>{user.activeStatus}</p>
      
      {/* Stats */}
      <div className={styles.statsContainer}>
        <div className={styles.statItem}>
          <div className={styles.statNumber}>{user.friends}</div>
          <div className={styles.statLabel}>Friends</div>
        </div>
        <div className={styles.statItem}>
          <div className={styles.statNumber}>{user.groups}</div>
          <div className={styles.statLabel}>Groups</div>
        </div>
      </div>
      
      {/* Action Button */}
      <button 
        onClick={() => onAction(user.id, user.actionType)}
        className={`${styles.actionButton} ${
          user.actionType === 'profile' 
            ? styles.profileButton 
            : styles.cancelButton
        }`}
      >
        {user.actionText}
      </button>
    </div>
  );
};

export default PeopleCard;