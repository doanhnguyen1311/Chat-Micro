import {
    UserRound,
    Users2,
    MonitorDot,
    SquareUserRound,
    EllipsisVertical,
    BellRing,
    MessageCircleMore,
    Settings,
} from 'lucide-react';
import TabItem from '../TabItem';
import styles from '../index.module.css';
import { useState } from 'react';
import { Link } from 'react-router-dom';

export default function NavigationTabs() {
    const [showMenu, setShowMenu] = useState(false);
    
    return (
        <div className={styles.navigationTabs}>
            <TabItem label="Activity" icon={<MonitorDot size={24} />} to="/my-activity" />
            <TabItem label="Profile" icon={<SquareUserRound size={24} />} to="/my-profile" />
            <TabItem label="Friends" icon={<UserRound size={24} />} badge="1" to="/my-friends" />
            <TabItem label="Groups" icon={<Users2 size={24} />} badge='5' to="/my-groups" />
            <div 
                className={styles.tabItem}
                onClick={() => setShowMenu(prev => !prev)}
            >
                <div className={styles.tabIcon}>
                    <EllipsisVertical size={24} />
                </div>
                <span className={styles.tabLabel}>More</span>
                {showMenu && (
                    <div className={styles.menu}>
                        <Link to='notifications' className={`${styles.menu_item} ${styles.menu_middle} d-flex align-center gap-8px`}><BellRing size={16}/>Notifications</Link>
                        <div className={`${styles.menu_item} d-flex align-center gap-8px`}><MessageCircleMore size={16}/>Messages</div>
                        <Link to='settings' className={`${styles.menu_item} d-flex align-center gap-8px`}><Settings size={16}/>Settings</Link>
                    </div>
                )}
            </div>
        </div>
    );
}
