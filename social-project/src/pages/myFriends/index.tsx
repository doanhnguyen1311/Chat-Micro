import type React from "react";
import { MessageCircleWarning } from "lucide-react";
import styles from "./index.module.css";

const MyFriends: React.FC = () => {
    return (
        <div className={`col-lg-6 border-x-primary ${styles.container}`}>
            {/* Tabs */}
            <div className={styles.tabs}>
                <div className={`${styles.tab} ${styles.active}`}>Friendships</div>
                <div className={styles.tab}>Requests</div>
            </div>

            <div className={styles.selectContainer}>
                <select className={styles.select}>
                    <option value=''>Last Active</option>
                    <option value=''>Newest Registered</option>
                    <option value=''>Alphabetical</option>
                </select>
            </div>

            <hr className={styles.divider} />

            <div className={styles.members}>
                <aside className={styles.messages}>
                    <MessageCircleWarning size={14} className={styles.icon}/>
                    <p>Sorry, no members were found.</p>
                </aside>
            </div>
        </div>
    );
};

export default MyFriends;
