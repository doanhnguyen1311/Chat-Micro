import React from 'react';
import styles from './index.module.css';

interface ActivityItem {
    id: number;
    user: string;
    action: string;
    target?: string;
    timestamp: string;
}

const RecentActivity: React.FC = () => {
    const activities: ActivityItem[] = [
        {
            id: 1,
            user: 'Joseph',
            action: 'posted a new activity',
            target: 'Comment',
            timestamp: '1 day, 13 hours ago'
        },
        {
            id: 2,
            user: 'Joseph',
            action: 'joined the group',
            target: 'Photography',
            timestamp: '1 day, 14 hours ago'
        },
        {
            id: 3,
            user: 'Joseph',
            action: 'posted a new activity',
            target: 'Comment',
            timestamp: '1 day, 14 hours ago'
        }
    ];

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <h3 className={styles.title}>Recent activity</h3>
                <div className={styles.underline}></div>
            </div>
            
            <div className={styles.activityList}>
                {activities.map((activity) => (
                    <div key={activity.id} className={styles.activityItem}>
                        <div className={styles.bullet}></div>
                        <div className={styles.content}>
                            <div className={styles.activityText}>
                                <span className={styles.username}>{activity.user}</span>{' '}
                                <span className={styles.action}>{activity.action}</span>
                                {activity.target && (
                                    <div className={styles.target}>{activity.target}</div>
                                )}
                            </div>
                            <div className={styles.timestamp}>{activity.timestamp}</div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default RecentActivity;