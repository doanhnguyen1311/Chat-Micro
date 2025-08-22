import type { ReactNode } from 'react';
import { Link, useLocation } from 'react-router-dom';
import styles from '../index.module.css';

interface TabItemProps {
    icon: ReactNode;
    label: string;
    badge?: string;
    to: string;
}

export default function TabItem({ icon, label, badge, to }: TabItemProps) {
    const location = useLocation();
    const isActive = (() => {
        const currentPath = location.pathname;
        
        // Nếu to là đường dẫn tương đối (không bắt đầu bằng /)
        if (!to.startsWith('/')) {
            return currentPath.endsWith(to);
        }
        
        // Nếu to là đường dẫn tuyệt đối
        if (currentPath === to) return true;
        
        // Xử lý trường hợp đặc biệt cho Home tab
        if (to.includes('group-home')) {
            // Active khi đường dẫn chứa group-home HOẶC chỉ là /group/:id
            return currentPath.includes('group-home') || 
                   (currentPath.match(/^\/group\/[^\/]+$/) !== null);
        }
        
        // Cho các tab khác, kiểm tra xem có chứa phần cuối của đường dẫn không
        const pathSegment = to.split('/').pop();
        return pathSegment ? currentPath.includes(pathSegment) : false;
    })();

    return (
        <Link to={to} className={styles.linkWrapper}>
            <div className={isActive ? styles.tabItemActive : styles.tabItem}>
                <div className={isActive ? styles.tabIconActive : styles.tabIcon}>
                    {icon}
                    {badge && <span className={isActive ? styles.badgeActive : styles.badge}>{badge}</span>}
                </div>
                <span className={isActive ? styles.tabLabelActive : styles.tabLabel}>{label}</span>
            </div>
        </Link>
    );
}
