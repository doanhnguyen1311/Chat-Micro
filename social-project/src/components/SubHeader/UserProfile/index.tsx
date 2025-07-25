import styles from '../index.module.css';
import avatar from '../../../assets/imgs/avagroup1.jpg';

export default function UserProfile() {
    return (
        <div className={styles.profileSection}>
            <div className={styles.avatarContainer}>
                <img src={avatar} alt="Profile" className={styles.avatar} />
                <h2 className={styles.userName}>Joseph</h2>
            </div>
            <div className={styles.userInfo}>
                <div className={styles.userMeta}>
                    <span className={styles.userTag}>@user</span>
                    <span className={styles.userActive}>Active 3 seconds ago</span>
                </div>
            </div>
        </div>
    );
}
