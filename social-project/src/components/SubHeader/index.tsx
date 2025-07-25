import styles from './index.module.css';
import UserProfile from './UserProfile';
import ActionButtons from './ActionButtons';
import NavigationTabs from './NavigationTabs';

export default function SubHeader() {
    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <UserProfile />
                <ActionButtons />
            </div>
            <NavigationTabs />
        </div>
    );
}
