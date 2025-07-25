import React from 'react';
import styles from './index.module.css';
import TotalStats from './TotalStats';
import PhotoGallery from './PhotoGallery';

const MyPhoto: React.FC = () => {
    return (
        <div className="col-lg-3 d-flex flex-column">
            <TotalStats />
            <div className={styles.photoContainer}>
                <div className={styles.widgetTitle}>My Photos</div>
                <PhotoGallery />
            </div>
        </div>
    );
};

export default MyPhoto;
