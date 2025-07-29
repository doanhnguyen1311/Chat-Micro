import React, { useState } from "react";
import styles from "./index.module.css";
import General from "./General";
import EmailNotifications from "./EmailNotifications";
import ProfileVisibilitySettings from "./ProfileVisibilitySettings";

const Settings: React.FC = () => {
    const [activeTab, setActiveTab] = useState<"general" | "email" | "visibility" | "invite">("general");

    return (
        <div className={`col-lg-6 border-x-primary ${styles.container}`}>
            <div className={styles.tabs}>
                <div
                    className={`${styles.tab} ${activeTab === "general" ? styles.active : ""}`}
                    onClick={() => setActiveTab("general")}
                >
                    General
                </div>
                <div
                    className={`${styles.tab} ${activeTab === "email" ? styles.active : ""}`}
                    onClick={() => setActiveTab("email")}
                >
                    Email
                </div>
                <div
                    className={`${styles.tab} ${activeTab === "visibility" ? styles.active : ""}`}
                    onClick={() => setActiveTab("visibility")}
                >
                    Profile Visibility
                </div>
            </div>

            {/* Nội dung của từng tab */}
            {activeTab === "general" && <General />}
            {activeTab === "email" && <EmailNotifications />}
            {activeTab === "visibility" && <ProfileVisibilitySettings />}
        </div>
    );
};

export default Settings;
