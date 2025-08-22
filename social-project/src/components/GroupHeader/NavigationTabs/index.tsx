import {
    Users2,
    MonitorDot,
    UserRoundPlus,
    Images
} from 'lucide-react';
import TabItem from '../TabItem';
import styles from '../index.module.css';
import { useParams } from 'react-router-dom';

export default function NavigationTabs() {
    const { id } = useParams<{ id: string }>();
    
    return (
        <div className={styles.navigationTabs}>
            <TabItem label="Home" icon={<MonitorDot size={24} />} to={`/group/${id}/group-home`} />
            <TabItem label="Members" icon={<Users2 size={24} />} to={`/group/${id}/group-members`} />
            <TabItem label="Invite" icon={<UserRoundPlus size={24} />} badge="1" to={`/group/${id}/group-invite`}  />
            <TabItem label="Media" icon={<Images size={24} />} badge='5' to={`/group/${id}/group-media`}  />
        </div>
    );
}
