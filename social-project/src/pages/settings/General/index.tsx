import React from "react";
import styles from "../index.module.css";
import { MessageCircleWarning, EyeOff } from "lucide-react";

const General: React.FC = () => {
    return (
        <div>
            <h2 className={styles.title}>Email & Password</h2>
            <h4 className={styles.section}>Update your email and or password.</h4>

            <form className={styles.form}>
                {/* Current Password */}
                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit}>Current Password <span className={styles.required}>(required to update email or change current password)</span></label>
                        <input type="text" className={styles.input} />
                        <div className={styles.forgot}>Lost your password?</div>
                    </div>
                    
                </div>

                {/* Account Email */}
                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit}>Account Email</label>
                        <input type="text" className={styles.input} defaultValue='tinnguyen210303@gmail.com'/>
                    </div>
                </div>

                <div className={styles.infoBox}>
                    <MessageCircleWarning size={16} style={{marginTop: '6px', color: '#8224e3'}} />
                    <p>
                        Click on the "Generate Password" button to change your password.
                    </p>
                </div>

                <button className={styles.selectFile}>Generate Password</button>

                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit}>Add Your New Password</label>
                        <div className="d-flex align-center gap-8px">
                            <input type="text" className={styles.input} style={{width: '42%'}}/>
                            <EyeOff size={20} className={styles.seeIcon}/>
                            <button className={styles.cancelBtn}>Cancel</button>
                        </div>
                    </div>
                </div>

                <button type="submit" className={styles.saveButton}>Save Changes</button>
            </form>
        </div>
    );
};

export default General;
