import React from "react";
import { MessageCircleWarning } from "lucide-react";
import styles from "../index.module.css";

const ChangeCover: React.FC = () => {

    return (
        <div className={styles.changePhoto}>
            {/* Title */}
            <h2 className={styles.title}>Change Cover Image</h2>

            {/* Info Box */}
            <div className={styles.infoBox}>
                <MessageCircleWarning size={16} style={{marginTop: '6px', color: '#8224e3'}} />
                <p>
                    Your Cover Image will be used to customize the header of your profile.
                </p>
            </div>

            {/* Select File Box */}
            <div className={styles.dropzone}>
                <p>Drop your file here</p>
                <button className={styles.selectFile}>Select your file</button>
            </div>

            {/* Warning */}
            <div className={styles.warningBox}>
                <p className={styles.text}>
                    For better results, make sure to upload an image that is larger than 1920px wide, and 280px tall.
                </p>
            </div>
        </div>
    );
};

export default ChangeCover;
