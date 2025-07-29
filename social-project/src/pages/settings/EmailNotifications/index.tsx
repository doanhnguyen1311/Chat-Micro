import React from "react";
import styles from "./index.module.css";

type NotificationOption = {
    label: string;
    name: string;
};

type NotificationCategory = {
    title: string;
    options: NotificationOption[];
};

const notificationSettings: NotificationCategory[] = [
    {
        title: "Activity",
        options: [
            {
                label: 'A member mentions you in an update using "@user"',
                name: "mention",
            },
            {
                label: "A member replies to an update or comment you've posted",
                name: "reply",
            },
        ],
    },
    {
        title: "Messages",
        options: [
            {
                label: "A member sends you a new message",
                name: "message",
            },
        ],
    },
    {
        title: "Friends",
        options: [
            {
                label: "A member sends you a friendship request",
                name: "friendRequest",
            },
            {
                label: "A member accepts your friendship request",
                name: "friendAccept",
            },
        ],
    },
    {
        title: "Groups",
        options: [
            {
                label: "A member invites you to join a group",
                name: "groupInvite",
            },
            {
                label: "Group information is updated",
                name: "groupUpdate",
            },
            {
                label: "You are promoted to a group administrator or moderator",
                name: "groupPromote",
            },
            {
                label:
                "A member requests to join a private group for which you are an admin",
                name: "groupRequest",
            },
            {
                label: "Your request to join a group has been approved or denied",
                name: "groupApproval",
            },
        ],
    },
];

const EmailNotifications: React.FC = () => {
    return (
        <form className={styles.container}>
            <h2 className={styles.heading}>Email Notifications</h2>
            <p className={styles.subheading}>Set your email notification preferences.</p>

            {notificationSettings.map((category) => (
                <div key={category.title}>
                    <h4 className={styles.category}>{category.title}</h4>
                    {category.options.map((option) => (
                        <div key={option.name} className={styles.row}>
                            <span className={styles.label}>{option.label}</span>
                            <div className={styles.radioGroup}>
                                <label className={styles.radioLabel}>
                                    <input type="radio" name={option.name} defaultChecked />
                                    <span>Yes</span>
                                </label>
                                <label className={styles.radioLabel}>
                                    <input type="radio" name={option.name} />
                                    <span>No</span>
                                </label>
                            </div>
                        </div>
                    ))}
                </div>
            ))}

            <button type="submit" className={styles.saveButton}>
                Save Changes
            </button>
        </form>
    );
};

export default EmailNotifications;
