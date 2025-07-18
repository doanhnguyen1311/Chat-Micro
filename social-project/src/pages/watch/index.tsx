import React, { useState } from 'react';
import { Search } from 'lucide-react';
import styles from './index.module.css';
import image from '../../assets/imgs/video.jpg';
import type { Video } from './VideoCard';
import VideoCard from './VideoCard';

const Watch: React.FC = () => {
    const [searchTerm, setSearchTerm] = useState('');

    const videos: Video[] = [
        {
            id: '1',
            thumbnail: image,
            duration: '0:09',
            author: {
                name: 'Joseph',
                avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
            },
        },
        {
            id: '2',
            thumbnail: image,
            duration: '0:09',
            author: {
                name: 'Joseph',
                avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
            },
        },
        {
            id: '3',
            thumbnail: image,
            duration: '0:06',
            author: {
                name: 'Robert Cook',
                avatar: 'https://randomuser.me/api/portraits/men/2.jpg',
            },
        },
        {
            id: '4',
            thumbnail: image,
            duration: '0:34',
            author: {
                name: 'Joseph',
                avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
            },
        },
        {
            id: '5',
            thumbnail: image,
            duration: '0:30',
            author: {
                name: 'Joseph',
                avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
            },
        },
        {
            id: '6',
            thumbnail: image,
            duration: '0:00',
            author: {
                name: 'Robert Cook',
                avatar: 'https://randomuser.me/api/portraits/men/2.jpg',
            },
        },
    ];

    const filteredVideos = videos.filter((video) =>
        video.author.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="col-lg-8 p-36">
            <div className={styles.header}>
                <div className={styles.headerTop}>
                    <h1 className={styles.title}>All Videos</h1>
                </div>

                <div className={styles.searchContainer}>
                    <input
                        type="text"
                        placeholder="Search Media"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className={styles.searchInput}
                    />
                    <button className={styles.searchButton}>
                        <Search className={styles.searchIcon} />
                    </button>
                </div>
            </div>

            <div className={styles.videoGrid}>
                {filteredVideos.map((video) => (
                    <VideoCard key={video.id} video={video} />
                ))}
            </div>

            {filteredVideos.length === 0 && (
                <div className={styles.emptyState}>
                    <div className={styles.emptyTitle}>No videos found</div>
                    <div className={styles.emptySubtitle}>Try adjusting your search terms</div>
                </div>
            )}
        </div>
    );
};

export default Watch;
