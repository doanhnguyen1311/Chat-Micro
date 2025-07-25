import React from 'react';
import styles from '../index.module.css';

interface PhotoItemProps {
    src: string;
}

const PhotoItem: React.FC<PhotoItemProps> = ({ src }) => {
    return (
        <li className={styles.memberPhotoItem}>
            <img src={src} alt="Photo" className={styles.memberPhoto} />
        </li>
    );
};

export default PhotoItem;
