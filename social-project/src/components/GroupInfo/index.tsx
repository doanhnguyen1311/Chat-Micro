import React from 'react';
import avatar from '../../assets/imgs/avagroup1.jpg';
import styles from './index.module.css';

interface Member {
  id: string;
  name: string;
  avatar: string;
}

interface GroupCardProps {
  title?: string;
  description?: string;
  members?: Member[];
  maxVisibleMembers?: number;
}

const GroupInfo: React.FC<GroupCardProps> = ({
  title = "About Group",
  description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tem...",
  members = [
    { id: '1', name: 'Member 1', avatar: avatar },
    { id: '2', name: 'Member 2', avatar: avatar },
    { id: '3', name: 'Member 3', avatar: avatar },
    { id: '4', name: 'Member 4', avatar: avatar },
    { id: '5', name: 'Member 5', avatar: avatar },
    { id: '6', name: 'Member 6', avatar: avatar },
  ],
  maxVisibleMembers = 6
}) => {
  const handleReadMore = () => {
    console.log('Read more clicked');
  };

  const visibleMembers = members.slice(0, maxVisibleMembers);
  const remainingCount = members.length - maxVisibleMembers;

  return (
    <div className={styles.groupCard}>
      <div className={styles.header}>
        <h3 className={styles.title}>{title}</h3>
        <div className={styles.underline}></div>
      </div>
      
      <div className={styles.content}>
        <p className={styles.description}>{description}</p>
        <button className={styles.readMore} onClick={handleReadMore}>
          Read more
        </button>
      </div>

      <div className={styles.membersSection}>
        <h4 className={styles.membersTitle}>Newest Members</h4>
        <div className={styles.underline}></div>
        
        <div className={styles.avatarList}>
          {visibleMembers.map((member, index) => (
            <div 
              key={member.id} 
              className={styles.avatarWrapper}
              style={{ zIndex: visibleMembers.length - index }}
            >
              <img
                src={member.avatar}
                alt={member.name}
                className={styles.avatar}
              />
            </div>
          ))}
          {remainingCount > 0 && (
            <div className={styles.avatarWrapper}>
              <div className={styles.moreCount}>
                +{remainingCount}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default GroupInfo;