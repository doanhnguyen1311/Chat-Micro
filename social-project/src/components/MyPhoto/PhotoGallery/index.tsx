import React from 'react';
import styles from '../index.module.css';
import PhotoItem from '../PhotoItem';
import photo from '../../../assets/imgs/tindepchai.jpg';
import photo1 from '../../../assets/imgs/avagroup1.jpg';
import photo2 from '../../../assets/imgs/video.jpg';

const photos = [
    photo,
    photo1,
    photo2,
    photo1, 
    photo2,
    photo,
    photo,
    photo1,
    photo2,
];

const PhotoGallery: React.FC = () => {
    return (
        <ul className={styles.memberPhotoList}>
            {photos.map((img, idx) => (
                <PhotoItem key={idx} src={img} />
            ))}
        </ul>
    );
};

export default PhotoGallery;
