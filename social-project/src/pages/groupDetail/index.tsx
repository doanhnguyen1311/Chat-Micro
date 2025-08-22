import type React from "react";
import Editor from "../newsfeed/Editor";
import Activity from "../newsfeed/Activity";
import styles from "./index.module.css";

const GroupDetail: React.FC = () => {
    return (
        <div className={`col-lg-6 border-x-primary ${styles.container}`}>
            <Editor />
            <Activity />
        </div>
    );
};

export default GroupDetail;
