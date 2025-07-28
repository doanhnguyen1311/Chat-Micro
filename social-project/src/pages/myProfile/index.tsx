import React, { useState } from "react";
import styles from "./index.module.css";
import ViewProfile from "./ViewProfile";
import EditProfile from "./EditProfile";
import ChangePhoto from "./ChangePhoto";
import ChangeCover from "./ChangeCover";

const MyProfile: React.FC = () => {
    const [activeTab, setActiveTab] = useState<"view" | "edit" | "photo" | "cover">("view");

    return (
        <div className={`col-lg-6 border-x-primary ${styles.container}`}>
            <div className={styles.tabs}>
                <div
                    className={`${styles.tab} ${activeTab === "view" ? styles.active : ""}`}
                    onClick={() => setActiveTab("view")}
                >
                    View
                </div>
                <div
                    className={`${styles.tab} ${activeTab === "edit" ? styles.active : ""}`}
                    onClick={() => setActiveTab("edit")}
                >
                    Edit
                </div>
                <div
                    className={`${styles.tab} ${activeTab === "photo" ? styles.active : ""}`}
                    onClick={() => setActiveTab("photo")}
                >
                    Change Profile Photo
                </div>
                <div
                    className={`${styles.tab} ${activeTab === "cover" ? styles.active : ""}`}
                    onClick={() => setActiveTab("cover")}
                >
                    Change Cover Image
                </div>
            </div>

            {/* Nội dung của từng tab */}
            {activeTab === "view" && <ViewProfile />}
            {activeTab === "edit" && <EditProfile />}
            {activeTab === "photo" && <ChangePhoto />}
            {activeTab === "cover" && <ChangeCover />}
        </div>
    );
};

export default MyProfile;
