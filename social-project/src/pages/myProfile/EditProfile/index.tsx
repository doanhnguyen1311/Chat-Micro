import React from "react";
import styles from "../index.module.css";

const EditProfile: React.FC = () => {
    return (
        <div>
            <h2 className={styles.title}>Edit Profile</h2>
            <h4 className={styles.section}>Editing "Base" Profile Group</h4>
            <hr className={styles.divider} />

            <form className={styles.form}>
                {/* Name */}
                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit}>Name <span className={styles.required}>(required)</span></label>
                        <input type="text" className={styles.input} defaultValue="Joseph" />
                        <div className={styles.note}>This field may be seen by: <strong>Everyone</strong></div>
                    </div>
                </div>

                {/* Date of Birth */}
                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit}>Date of Birth <span className={styles.required}>(required)</span></label>
                        <div className={styles.dobGroup}>
                            <select defaultValue="7"><option>7</option></select>
                            <select defaultValue="February"><option>February</option></select>
                            <select defaultValue="1992"><option>1992</option></select>
                        </div>
                        <div className={styles.note}>This field may be seen by: <strong>Only Me</strong> <a className={styles.changeLink}>Change</a></div>
                    </div>
                </div>

                {/* Sex */}
                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit} style={{marginTop: '6px'}}>Sex <span className={styles.required}>(required)</span></label>
                        <div className={styles.radioGroup}>
                            <label><input type="radio" name="sex" defaultChecked /> Male</label>
                            <label><input type="radio" name="sex" /> Female</label>
                        </div>
                        <div className={styles.note}>This field may be seen by: <strong>Only Me</strong> <a className={styles.changeLink}>Change</a></div>
                    </div>
                </div>

                {/* City */}
                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit}>City <span className={styles.required}>(required)</span></label>
                        <input type="text" className={styles.input} defaultValue="aaa" />
                        <div className={styles.note}>This field may be seen by: <strong>Only Me</strong> <a className={styles.changeLink}>Change</a></div>
                    </div>
                </div>

                {/* Country */}
                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit}>Country <span className={styles.required}>(required)</span></label>
                        <select className={styles.select} defaultValue="Armenia">
                            <option>Armenia</option>
                        </select>
                        <div className={styles.note}>This field may be seen by: <strong>Only Me</strong> <a className={styles.changeLink}>Change</a></div>
                    </div>
                </div>

                <button type="submit" className={styles.saveButton}>Save Changes</button>
            </form>
        </div>
    );
};

export default EditProfile;
