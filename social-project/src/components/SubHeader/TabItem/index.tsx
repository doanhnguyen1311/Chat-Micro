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
    const isActive = location.pathname === to;

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
