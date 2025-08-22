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
            <button
                className={styles.actionButton}
                onMouseEnter={handleMouseEnter}
            >
                Leave Group
            </button>
        </div>
    );
}
