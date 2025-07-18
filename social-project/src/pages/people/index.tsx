import React, { useState } from 'react';
import { Search } from 'lucide-react';
import type { User } from './PeopleCard';
import PeopleCard from './PeopleCard';
import styles from './index.module.css';

const People: React.FC = () => {
    const [searchTerm, setSearchTerm] = useState('');
    
    const sampleUsers: User[] = [
        {
            id: 1,
            name: 'Joseph',
            avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop&crop=face',
            activeStatus: 'Active 3 seconds ago',
            friends: 0,
            groups: 3,
            actionType: 'profile',
            actionText: 'My Profile'
        },
        {
            id: 2,
            name: 'Sephiroth',
            avatar: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&h=150&fit=crop&crop=face',
            activeStatus: 'Active 3 weeks, 4 days ago',
            friends: 10,
            groups: 6,
            actionType: 'cancel',
            actionText: 'Cancel Friendship Request'
        },
        {
            id: 3,
            name: 'Linda Lohan',
            avatar: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&h=150&fit=crop&crop=face',
            activeStatus: 'Active 1 year, 5 months ago',
            friends: 6,
            groups: 5,
            actionType: 'cancel',
            actionText: 'Cancel Friendship Request'
        },
        {
            id: 4,
            name: 'Irina Petrova',
            avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&h=150&fit=crop&crop=face',
            activeStatus: 'Active 1 year, 5 months ago',
            friends: 10,
            groups: 3,
            actionType: 'cancel',
            actionText: 'Cancel Friendship Request'
        }
    ];
    
    const filteredUsers: User[] = sampleUsers.filter(user =>
        user.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
      
    const handleAction = (userId: number, actionType: string): void => {
        console.log(`Action ${actionType} for user ${userId}`);
    };

    return (
        <div className='col-lg-8 p-36'>
            {/* Header */}
            <div className={styles.header}>
                <div className={styles.headerTop}>
                    <h1 className={styles.title}>
                        Active Members
                    </h1>
                </div>
                
                <div className={styles.headerBottom}>
                    {/* Search Bar */}
                    <div className={styles.searchContainer}>
                        <input
                            type="text"
                            placeholder="Search Members..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className={styles.searchInput}
                        />
                        <button className={styles.searchButton}>
                            <Search className={styles.searchIcon} />
                        </button>
                    </div>
                    {/* Select filter */}
                    <div>
                        <select className={styles.selectFilter}>
                            <option value="">Last Active</option>
                            <option value="">Newest Registered</option>
                            <option value="">Alphabetical</option>
                        </select>
                    </div>
                </div>
            </div>

            {/* PeopleCard */}
            <div className={styles.peopleGrid}>
                {filteredUsers.map((user) => (
                    <PeopleCard 
                        key={user.id} 
                        user={user} 
                        onAction={handleAction}
                    />
                ))}
            </div>
        </div>
    );
};

export default People;