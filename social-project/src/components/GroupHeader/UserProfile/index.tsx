import { useAuth } from '../../../hooks/useAuth';
import styles from '../index.module.css';

export default function UserProfile() {
    const { user } = useAuth();

    return (
        <div className={styles.profileSection}>
            <div className={styles.avatarContainer}>
                <img src={user?.profile.avatarUrl} alt="Profile" className={styles.avatar} />
                <h2 className={styles.userName}>{user?.profile.fullName}</h2>
            </div>
            <div className={styles.userInfo}>
                <div className={styles.userMeta}>
                    <span className={styles.userTag}>{user?.username}</span>
                    <span className={styles.userActive}>Active 3 seconds ago</span>
                </div>
            </div>
        </div>
    );
}
