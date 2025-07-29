import React, { useState } from "react";
import styles from "./index.module.css";

const options = ["Everyone", "Only Me", "All Members", "My Friends"];

const defaultSettings = {
    name: "Everyone",
    PersonalInfo: "Only Me",
};

const ProfileVisibilitySettings: React.FC = () => {
    const [visibility, setVisibility] = useState(defaultSettings);

    const handleChange = (field: keyof typeof visibility, value: string) => {
        setVisibility((prev) => ({ ...prev, [field]: value }));
    };

    return (
        <form className={styles.container}>
            <h2 className={styles.heading}>Profile Visibility Settings</h2>
            <p className={styles.subheading}>
                Select who may see your profile details.
            </p>

            <table className={styles.table}>
                <thead>
                    <tr>
                        <th className={`${styles.th} ${styles.th1}`}>Base</th>
                        <th className={styles.th}>Visibility</th>
                    </tr>
                </thead>
                <tbody>
                <tr className={styles.row}>
                    <td className={styles.td}>Name</td>
                    <td className={`${styles.td} ${styles.td1}`}>Everyone</td>
                </tr>
                {Object.entries(visibility).map(([key, value]) => {
                    if (key === "name") return null;
                    return (
                    <tr key={key} className={styles.row}>
                        <td className={styles.td}>
                            {key
                                .replace(/([A-Z])/g, " $1")
                                .replace(/^./, (c) => c.toUpperCase())}
                        </td>
                        <td className={`${styles.td} ${styles.td1}`}>
                            <select
                                className={styles.select}
                                value={value}
                                onChange={(e) =>
                                    handleChange(key as keyof typeof visibility, e.target.value)
                                }
                            >
                                {options.map((opt) => (
                                    <option key={opt} value={opt}>
                                        {opt}
                                    </option>
                                ))}
                            </select>
                        </td>
                    </tr>
                    );
                })}
                </tbody>
            </table>

            <button type="submit" className={styles.saveButton}>
                Save Changes
            </button>
        </form>
    );
};

export default ProfileVisibilitySettings;
