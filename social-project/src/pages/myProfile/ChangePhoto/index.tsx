import React from "react";
import { MessageCircleWarning } from "lucide-react";
import styles from "../index.module.css";

const ChangePhoto: React.FC = () => {

    return (
        <div className={styles.changePhoto}>
            {/* Title */}
            <h2 className={styles.title}>Change Profile Photo</h2>

            {/* Info Box */}
            <div className={styles.infoBox}>
                <MessageCircleWarning size={40} className={styles.icon} />
                <p>
                    Your profile photo will be used on your profile and throughout the site. If there is a{" "}
                    <a href="#" className={styles.link}>Gravatar</a> associated with your account email we will use that, or you can upload an image from your computer.
                </p>
            </div>

            <div className={styles.dropzone}>
                <p>Drop your file here</p>
                <button className={styles.selectFile}>Select your file</button>
            </div>
        </div>
    );
};

export default ChangePhoto;
