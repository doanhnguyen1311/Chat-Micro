import React, { useEffect, useRef, useState } from 'react';
import { EllipsisVertical, ThumbsUp, CornerUpLeft, MessageCircleMore } from 'lucide-react';
import image from '../../../assets/imgs/tindepchai.jpg';
import like from '../../../assets/imgs/like.jpg';
import love from '../../../assets/imgs/love.png';
import haha from '../../../assets/imgs/haha.jpg';
import care from '../../../assets/imgs/care.jpg';
import wow from '../../../assets/imgs/wow.jpg';
import sad from '../../../assets/imgs/sad.jpg';
import angry from '../../../assets/imgs/angry.jpg';
import { useAuth } from '../../../hooks/useAuth';
import styles from '../index.module.css';

const reactions = [
    { name: "like", icon: like },
    { name: "love", icon: love },
    { name: "care", icon: care },
    { name: "haha", icon: haha },
    { name: "wow", icon: wow },
    { name: "sad", icon: sad },
    { name: "angry", icon: angry },
];

const Activity: React.FC = () => {
    const { user } = useAuth();

    const [showMenu, setShowMenu] = useState(false);

    const menuRef = useRef<HTMLDivElement>(null);

    const [isExpanded, setIsExpanded] = useState(false);

    const [selectedReaction, setSelectedReaction] = useState<string | null>(null);

    const [showReactions, setShowReactions] = useState(false);

    const timeoutRef = useRef<number | null>(null);

    const handleMouseEnter = () => {
        if (timeoutRef.current) {
            clearTimeout(timeoutRef.current);
        }
        setShowReactions(true);
    };

    const handleMouseLeave = () => {
        timeoutRef.current = window.setTimeout(() => {
            setShowReactions(false);
        }, 200);
    };

    const currentReaction = reactions.find(r => r.name === selectedReaction);

    const [showShareMenu, setShowShareMenu] = useState(false);

    const shareMenuRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        function handleClickOutside(event: MouseEvent) {
            if (
                menuRef.current &&
                !menuRef.current.contains(event.target as Node)
            ) {
                setShowMenu(false);
            }

            if (
                shareMenuRef.current &&
                !shareMenuRef.current.contains(event.target as Node)
            ) {
                setShowShareMenu(false);
            }
        }

        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);
    
    return (
        <div className={styles.activity}>
            <ul className={styles.activity_list}>
                <li className={styles.activity_item}>
                    <div className={styles.activity_avatar}>
                        <img
                            src={user?.profile.avatarUrl}
                            alt="avatar"
                            className={styles.item_avatar}
                        />
                    </div>
                    <div className={styles.activity_content}>
                        <div className={styles.activity_header} >
                            <div className={styles.post_meta}>
                                <p><span>{user?.username}</span> posted an update</p>
                                <div className={styles.date}> 2 days, 15 hours ago</div>
                            </div>
                            <div 
                                className={styles.more_icon}
                                ref={menuRef}
                                onClick={() => setShowMenu((prev) => !prev)}
                            >
                                <EllipsisVertical size={16} />
                            </div>
                            {showMenu && (
                                <div className={styles.menu}>
                                    <div className={`${styles.menu_item} ${styles.menu_first}`}>Mark as Favorite</div>
                                    <div className={`${styles.menu_item} ${styles.menu_second}`}>Delete</div>
                                </div>
                            )}
                        </div>
                        <div className={styles.activity_inner}>
                            <div className={styles.activity_container}>
                                <div className={styles.activity_inner_text}>
                                    <span>hello</span>
                                </div>
                                <ul className={styles.activity_media_list}>
                                    <li className={styles.media_list_item}>
                                        <div className={styles.media_item_thumbnail}>
                                            <img src={image} alt='anh'/>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div className={styles.activity_action}>
                            <div 
                                className={`${styles.generic_button_reaction} ${styles.reactionWrapper}`}
                                onMouseEnter={handleMouseEnter}
                                onMouseLeave={handleMouseLeave}
                                onClick={() => {
                                    if (selectedReaction) {
                                        setSelectedReaction(null);
                                    } else {
                                        setSelectedReaction("like");
                                    }
                                }}
                            >
                                <div className="d-flex align-center gap-4px">
                                    {selectedReaction  ? (
                                        <img
                                            src={currentReaction?.icon}
                                            alt={selectedReaction}
                                            className={styles.reaction_icon_selected}
                                        />
                                    ) : (
                                        <ThumbsUp size={16} />
                                    )}
                                    <span>
                                        {selectedReaction
                                            ? selectedReaction.charAt(0).toUpperCase() + selectedReaction.slice(1)
                                            : "Like"}
                                    </span>
                                </div>
                                {showReactions && (
                                    <div className={styles.reaction_popup}>
                                        {reactions.map(reaction => (
                                            <img
                                                key={reaction.name}
                                                src={reaction.icon}
                                                alt={reaction.name}
                                                className={styles.reaction_icon}
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    setSelectedReaction(prev =>
                                                        prev === reaction.name ? null : reaction.name
                                                    );
                                                    setShowReactions(false);
                                                }}
                                            />
                                        ))}
                                    </div>
                                )}
                            </div>
                            <div className={styles.generic_button_comment}>
                                <div 
                                    className='d-flex align-center gap-4px'
                                    onClick={() => setIsExpanded(true)}
                                >
                                    <span>Comment</span>
                                    <span className={styles.comment_count}>0</span>
                                </div>
                            </div>
                            <div 
                                className={styles.generic_button_share}
                                onClick={() => setShowShareMenu((prev) => !prev)}
                                ref={shareMenuRef}
                            >
                                <span>Share</span>
                                {showShareMenu && (
                                    <div className={styles.shareMenu}>
                                        <div className={`${styles.menu_item} ${styles.menu_first} d-flex align-center gap-8px`}><CornerUpLeft size={16} />Share on Activity</div>
                                        <div className={`${styles.menu_item} ${styles.menu_second} d-flex align-center gap-8px`}><MessageCircleMore size={16}/>Share on DeliChat</div>
                                    </div>
                                )}
                            </div>
                        </div>
                        {isExpanded && (
                            <div className={styles.expandedSection}>
                                <div className={styles.comment_editor}>
                                    <div>
                                        <img
                                            src={user?.profile.avatarUrl}
                                            alt="avatar"
                                            className={styles.comment_avatar}
                                        />
                                    </div>
                                    <div className={styles.comment}>
                                        <input type="text" className={styles.comment_input}/>
                                        <div className={styles.buttons}>
                                            <button className={styles.postBtn}>
                                                Post
                                            </button>
                                            <button
                                                className={styles.cancelBtn}
                                                onClick={() => setIsExpanded(false)}
                                            >
                                                Cancel
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                    <div className={styles.activity_comments}>

                    </div>
                </li>
            </ul>
        </div>
    );
};

export default Activity;
