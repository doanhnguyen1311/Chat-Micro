import React from "react";
import styles from "../index.module.css";

const ViewProfile: React.FC = () => (
    <>
        <h2 className={styles.title}>View Profile</h2>
        <h4 className={styles.section}>Base</h4>
        <hr className={styles.divider} />
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
    </>
);

export default ViewProfile;
