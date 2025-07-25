import React from 'react';
import avagroup from '../../assets/imgs/avagroup1.jpg';
import ava from '../../assets/imgs/tindepchai.jpg';
import GroupCard, { type Group } from "../group/GroupCard";
import styles from "./index.module.css";

const MyGroups: React.FC = () => {

    const sampleGroups: Group[] = [
        {
            id: 1,
            name: 'Climate Change',
            coverImage: avagroup,
            icon: ava,
            activeStatus: 'Active a day ago',
            memberCount: 7,
            privacy: 'Public Group',
            joined: false,
            members: [
                {
                    id: 1,
                    avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=40&h=40&fit=crop&crop=face',
                    name: 'John'
                },
                {
                    id: 2,
                    avatar: 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=40&h=40&fit=crop&crop=face',
                    name: 'Jane'
                },
                {
                    id: 3,
                    avatar: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=40&h=40&fit=crop&crop=face',
                    name: 'Mike'
                },
                {
                    id: 4,
                    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=40&h=40&fit=crop&crop=face',
                    name: 'Sarah'
                },
                {
                    id: 5,
                    avatar: 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=40&h=40&fit=crop&crop=face',
                    name: 'David'
                }
            ]
        },
        {
            id: 2,
            name: 'Photography',
            coverImage: avagroup,
            icon: ava,
            activeStatus: 'Active 2 days ago',
            memberCount: 7,
            privacy: 'Public Group',
            joined: false,
            members: [
                {
                    id: 6,
                    avatar: 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=40&h=40&fit=crop&crop=face',
                    name: 'Emma'
                },
                {
                    id: 7,
                    avatar: 'https://images.unsplash.com/photo-1519244703995-f4e0f30006d5?w=40&h=40&fit=crop&crop=face',
                    name: 'Alex'
                },
                {
                    id: 8,
                    avatar: 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=40&h=40&fit=crop&crop=face',
                    name: 'Lisa'
                },
                {
                    id: 9,
                    avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=40&h=40&fit=crop&crop=face',
                    name: 'Tom'
                },
                {
                    id: 10,
                    avatar: 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=40&h=40&fit=crop&crop=face',
                    name: 'Anna'
                }
            ]
        }
    ];

    const handleJoinGroup = (groupId: number): void => {
        console.log(`Joining group ${groupId}`);
    };

    return (
        <div className={`col-lg-6 border-x-primary ${styles.container}`}>
            {/* Tabs */}
            <div className={styles.tabs}>
                <div className={`${styles.tab} ${styles.active}`}>Memberships</div>
                <div className={styles.tab}>Invitations</div>
            </div>

            <div className={styles.selectContainer}>
                <select className={styles.select}>
                    <option value=''>Last Active</option>
                    <option value=''>Newest Registered</option>
                    <option value=''>Alphabetical</option>
                </select>
            </div>

            <hr className={styles.divider} />

            {/* Groups Grid */}
            <div className={styles.groupsGrid}>
                {sampleGroups.map((group: Group) => (
                <GroupCard 
                    key={group.id} 
                    group={group} 
                    onJoinGroup={handleJoinGroup}
                />
                ))}
            </div>
        </div>
    );
};

export default MyGroups;
