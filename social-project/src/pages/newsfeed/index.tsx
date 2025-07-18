import React from 'react';
import Editor from './Editor';
import Activity from './Activity';

const NewsFeed: React.FC = () => {
    return (
        <div className="col-lg-8 p-36">
            <Editor />
            <Activity />
        </div>
    );
};

export default NewsFeed;
