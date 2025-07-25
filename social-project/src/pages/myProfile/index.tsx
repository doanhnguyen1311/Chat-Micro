import type React from "react";
import styles from "./index.module.css";

const MyProfile: React.FC = () => {
    return (
        <div className={`col-lg-6 border-x-primary ${styles.container}`}>
            {/* Tabs */}
            <div className={styles.tabs}>
                <div className={`${styles.tab} ${styles.active}`}>View</div>
                <div className={styles.tab}>Edit</div>
                <div className={styles.tab}>Change Profile Photo</div>
                <div className={styles.tab}>Change Cover Image</div>
            </div>

            {/* Title */}
            <h2 className={styles.title}>View Profile</h2>

            {/* Section */}
            <h4 className={styles.section}>Base</h4>
            <hr className={styles.divider} />

            {/* Info Table */}
            <table className={styles.table}>
                <tbody>
                    <tr>
                        <td className={styles.label}>Name</td>
                        <td className={styles.value}>Joseph</td>
                    </tr>
                    <tr>
                        <td className={styles.label}>Date of Birth</td>
                        <td className={styles.value}>1992-02-07</td>
                    </tr>
                    <tr>
                        <td className={styles.label}>Sex</td>
                        <td className={`${styles.value} ${styles.highlight}`}>Male</td>
                    </tr>
                    <tr>
                        <td className={styles.label}>City</td>
                        <td className={styles.value}>aaa</td>
                    </tr>
                    <tr>
                        <td className={styles.label}>Country</td>
                        <td className={`${styles.value} ${styles.highlight}`}>Armenia</td>
                    </tr>
                </tbody>
            </table>
        </div>
    );
};

export default MyProfile;
