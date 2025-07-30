import React from 'react';
import styles from './index.module.css';
import { Eye, X } from 'lucide-react';

interface Notification {
    id: number;
    message: string;
    date: string;
    isRead: boolean;
}

const notifications: Notification[] = [
    { id: 1, message: 'Sephiroth accepted your friendship request', date: '1 month, 1 week ago', isRead: false },
    { id: 2, message: 'Joseph mentioned you', date: '2 months, 3 weeks ago', isRead: false },
    { id: 3, message: 'Sephiroth accepted your friendship request', date: '5 months, 2 weeks ago', isRead: false },
];

const NotificationPanel: React.FC = () => {
    return (
        <div className={`col-lg-6 border-x-primary ${styles.container}`}>
            <div className={styles.tabs}>
                <span className={`${styles.tab} ${styles.active}`}>Unread</span>
                <span className={styles.tab}>Read</span>
            </div>

            <div className={styles.filter}>
                <select className={styles.select}>
                    <option>— Everything —</option>
                </select>
            </div>

            <table className={styles.table}>
                <thead>
                    <tr>
                        <th><input type="checkbox" /></th>
                        <th>Notification</th>
                        <th>Date Received ▲</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {notifications.map(n => (
                        <tr key={n.id}>
                            <td><input type="checkbox" /></td>
                            <td className={styles.message}>{n.message}</td>
                            <td className={styles.date}>{n.date}</td>
                            <td className={styles.actions}>
                                <Eye size={14} className={styles.icon} />
                                /
                                <X size={14} className={styles.icon} />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <div className={styles.bulkAction}>
                <select className={styles.select}>
                    <option>Bulk Actions</option>
                </select>
            </div>
        </div>
    );
};

export default NotificationPanel;
