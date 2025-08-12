import React from "react";
import { useAuth } from "../../../hooks/useAuth";
import styles from "../index.module.css";

const EditProfile: React.FC = () => {
    const { user } = useAuth();
    console.log(user);
    
    // Giả sử userData.birthday = "2003-02-07"
    const birthday = user?.profile.birthday;
    let day: number = 0;
    let month: string = "";
    let year: number = 0;

    if (birthday) {
        const dateObj = new Date(birthday);
        day = dateObj.getDate();
        month = dateObj.toLocaleString("en-US", { month: "long" });
        year = dateObj.getFullYear();
    }

    // Mảng ngày, tháng, năm
    const days = Array.from({ length: 31 }, (_, i) => i + 1);
    const months = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];
    const years = Array.from({ length: 100 }, (_, i) => new Date().getFullYear() - i);

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
                        <input type="text" className={styles.input} defaultValue={user?.profile.fullName} />
                        <div className={styles.note}>This field may be seen by: <strong>Everyone</strong></div>
                    </div>
                </div>

                {/* Date of Birth */}
                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit}>Date of Birth <span className={styles.required}>(required)</span></label>
                        <div className={styles.dobGroup}>
                            <select defaultValue={day || ""}>
                                <option value="">Day</option>
                                {days.map(d => (
                                    <option key={d} value={d}>{d}</option>
                                ))}
                            </select>

                            <select defaultValue={month || ""}>
                                <option value="">Month</option>
                                {months.map(m => (
                                    <option key={m} value={m}>{m}</option>
                                ))}
                            </select>

                            <select defaultValue={year || ""}>
                                <option value="">Year</option>
                                {years.map(y => (
                                    <option key={y} value={y}>{y}</option>
                                ))}
                            </select>
                        </div>
                        <div className={styles.note}>This field may be seen by: <strong>Only Me</strong></div>
                    </div>
                </div>

                {/* Sex */}
                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit} style={{marginTop: '6px'}}>Sex <span className={styles.required}>(required)</span></label>
                        <div className={styles.radioGroup}>
                            <label>
                                <input
                                    type="radio"
                                    name="sex"
                                    value="male"
                                    checked={user?.profile.gender === "male"}
                                /> Male
                            </label>
                            <label>
                                <input
                                    type="radio"
                                    name="sex"
                                    value="female"
                                    checked={user?.profile.gender === "female"}
                                /> Female
                            </label>
                        </div>
                        <div className={styles.note}>This field may be seen by: <strong>Only Me</strong></div>
                    </div>
                </div>

                {/* City */}
                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit}>City <span className={styles.required}>(required)</span></label>
                        <input type="text" className={styles.input} defaultValue={user?.profile.location} />
                        <div className={styles.note}>This field may be seen by: <strong>Only Me</strong></div>
                    </div>
                </div>

                {/* Country */}
                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit}>Country <span className={styles.required}>(required)</span></label>
                        <select className={styles.select} defaultValue={user?.profile.website}>
                            <option>Armenia</option>
                        </select>
                        <div className={styles.note}>This field may be seen by: <strong>Only Me</strong></div>
                    </div>
                </div>

                <button type="submit" className={styles.saveButton}>Save Changes</button>
            </form>
        </div>
    );
};

export default EditProfile;
