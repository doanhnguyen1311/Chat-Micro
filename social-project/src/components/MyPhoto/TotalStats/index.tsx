import React from 'react';
import styles from '../index.module.css';

const TotalStats: React.FC = () => {
    return (
        <div className={styles.totalJoin}>
            <div className={styles.totalFriends}>
                <span className={styles.friendCount}>0</span>
                <span className={styles.title}>Friends</span>
            </div>
            <div className={styles.totalGroups}>
                <span className={styles.groupsCount}>5</span>
                <span className={styles.title}>Groups</span>
            </div>
        </div>
    );
};

export default TotalStats;
