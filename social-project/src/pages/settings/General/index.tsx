import React, { useState } from "react";
import { MessageCircleWarning, EyeOff, Eye } from "lucide-react";
import { useAuth } from "../../../hooks/useAuth";
import styles from "../index.module.css";

const General: React.FC = () => {
    const { user } = useAuth();

    const [newPassword, setNewPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);

    // Hàm tạo mật khẩu ngẫu nhiên
    const generatePassword = () => {
        const chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()";
        let password = "";
        for (let i = 0; i < 10; i++) {
            password += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        setNewPassword(password);
    };

    // Hàm xóa mật khẩu
    const clearPassword = () => {
        setNewPassword("");
    };

    // Hàm toggle hiện/ẩn mật khẩu
    const togglePasswordVisibility = () => {
        setShowPassword(prev => !prev);
    };

    return (
        <div>
            <h2 className={styles.title}>Email & Password</h2>
            <h4 className={styles.section}>Update your email and or password.</h4>

            <div className={styles.form}>
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
                        <input type="text" className={styles.input} defaultValue={user?.email}/>
                    </div>
                </div>

                <div className={styles.infoBox}>
                    <MessageCircleWarning size={16} style={{marginTop: '6px', color: '#8224e3'}} />
                    <p>
                        Click on the "Generate Password" button to change your password.
                    </p>
                </div>

                <button className={styles.selectFile} onClick={generatePassword}>Generate Password</button>

                <div className={styles.formGroup}>
                    <div className={styles.fieldSet}>
                        <label className={styles.labelEdit}>Add Your New Password</label>
                        <div className="d-flex align-center gap-8px">
                            <input 
                                type={showPassword ? "text" : "password"} 
                                className={styles.input} 
                                style={{width: '42%'}} 
                                defaultValue={newPassword}
                            />
                            {showPassword ? (
                                <Eye
                                    size={20}
                                    className={styles.seeIcon}
                                    onClick={togglePasswordVisibility}
                                    style={{ cursor: "pointer" }}
                                />
                            ) : (
                                <EyeOff
                                    size={20}
                                    className={styles.seeIcon}
                                    onClick={togglePasswordVisibility}
                                    style={{ cursor: "pointer" }}
                                />
                            )}
                            <button className={styles.cancelBtn} onClick={clearPassword}>Cancel</button>
                        </div>
                    </div>
                </div>

                <button type="submit" className={styles.saveButton}>Save Changes</button>
            </div>
        </div>
    );
};

export default General;
