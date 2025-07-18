import React, { useState } from 'react';
import { Paperclip } from 'lucide-react';
import styles from '../index.module.css';

const Editor: React.FC = () => {
    const [isExpanded, setIsExpanded] = useState(false);
    return (
        <div className={styles.editor}>
                <div className={styles.container}>
                    <img
                        src="https://randomuser.me/api/portraits/women/44.jpg"
                        alt="Joseph"
                        className={styles.avatar}
                    />
                    <input
                        type="text"
                        placeholder="What's new, Joseph?"
                        className={styles.input}
                        onFocus={() => setIsExpanded(true)}
                    />
                </div>

                {isExpanded && (
                    <div className={styles.expandedSection}>
                        <div className={styles.actions}>
                            <button className={styles.attachBtn}>
                                <Paperclip size={16}/> Attach media
                            </button>
                            <div className={styles.bottomRow}>
                                <select className={styles.select}>
                                    <option>Post in: Profile</option>
                                    <option>Post in: Group</option>
                                </select>
                                <div className={styles.buttons}>
                                    <button
                                        className={styles.cancelBtn}
                                        onClick={() => setIsExpanded(false)}
                                    >
                                        Cancel
                                    </button>
                                    <button className={styles.postBtn}>
                                        Post Update
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </div>
    )
}

export default Editor;