import React from "react";
import { useAuth } from '../../../hooks/useAuth';
import styles from "../index.module.css";

const ViewProfile: React.FC = () => {
    const { user } = useAuth();

    return (
        <>
            <h2 className={styles.title}>View Profile</h2>
            <h4 className={styles.section}>Base</h4>
            <hr className={styles.divider} />
            <table className={styles.table}>
                <tbody>
                    <tr>
                        <td className={styles.label}>Name</td>
                        <td className={styles.value}>{user?.profile.fullName}</td>
                    </tr>
                    <tr>
                        <td className={styles.label}>Date of Birth</td>
                        <td className={styles.value}>{user?.profile.birthday}</td>
                    </tr>
                    <tr>
                        <td className={styles.label}>Sex</td>
                        <td className={`${styles.value} ${styles.highlight}`}>{user?.profile.gender}</td>
                    </tr>
                    <tr>
                        <td className={styles.label}>City</td>
                        <td className={styles.value}>{user?.profile.location}</td>
                    </tr>
                    <tr>
                        <td className={styles.label}>Website</td>
                        <td className={`${styles.value} ${styles.highlight}`}>{user?.profile.website}</td>
                    </tr>
                </tbody>
            </table>
        </>
    )
};

export default ViewProfile;
