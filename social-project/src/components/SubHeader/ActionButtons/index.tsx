import { SquarePen, ImageUp } from 'lucide-react';
import styles from '../index.module.css';

const hoverStyles = {
    backgroundColor: '#d1d5db'
};

const handleMouseEnter = (e: React.MouseEvent<HTMLElement>) => {
    Object.assign(e.currentTarget.style, hoverStyles);
};

export default function ActionButtons() {
    return (
        <div className={styles.actionButtons}>
            {[<SquarePen size={20} />, <ImageUp size={20} />].map((icon, idx) => (
                <button
                    key={idx}
                    className={styles.actionButton}
                    onMouseEnter={handleMouseEnter}
                >
                    {icon}
                </button>
            ))}
        </div>
    );
}
