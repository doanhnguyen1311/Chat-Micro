import React from 'react';
import { Play } from 'lucide-react';
import styles from '../index.module.css';

export interface Video {
    id: string;
    thumbnail: string;
    duration: string;
    author: {
        name: string;
        avatar: string;
    };
    title?: string;
}

interface VideoCardProps {
    video: Video;
}

const VideoCard: React.FC<VideoCardProps> = ({ video }) => {
    return (
        <div className={styles.videoCard}>
            <div className={styles.thumbnailContainer}>
                <img 
                    src={video.thumbnail} 
                    alt="Video thumbnail"
                    className={styles.thumbnail}
                />
                <div className={styles.duration}>{video.duration}</div>
                <div className={styles.playOverlay}>
                    <div className={styles.playButton}>
                        <Play className={styles.playIcon} />
                    </div>
                </div>
            </div>
            <div className={styles.cardContent}>
                <div className={styles.authorInfo}>
                    <img 
                        src={video.author.avatar} 
                        alt={video.author.name}
                        className={styles.avatar}
                    />
                    <div>
                        <h3 className={styles.authorName}>{video.author.name}</h3>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default VideoCard;
