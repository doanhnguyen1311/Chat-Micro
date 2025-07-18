// components/FriendListGroup.tsx
import React from "react";
import { MoreVertical } from "lucide-react";
import './index.css';

interface Friend {
  id: number;
  name: string;
  avatar?: string;
}

interface FriendListGroupProps {
  data: Record<string, Friend[]>; 
}

const getInitials = (name: string) => {
  return name
    .split(" ")
    .map((word) => word[0])
    .join("")
    .slice(0, 2)
    .toUpperCase();
};

const FriendListGroup: React.FC<FriendListGroupProps> = ({ data }) => {
  return (
    <div className="friend-list-group">
      {Object.keys(data).sort().map((letter) => (
        <div key={letter} className="mb-24">
          <div className="d-flex align-center gap-16px mb-8">
              <p className="text-green fs-14 fw-medium">{letter}</p>
              <div className="divided"></div>
          </div>
          {data[letter].map((friend) => (
            <div
              key={friend.id}
              className="d-flex justify-between align-center py-8 px-4 radius-4 hover:bg-hover"
            >
              <div className="d-flex align-center gap-16px">
                {friend.avatar ? (
                  <img
                    src={friend.avatar}
                    alt={friend.name}
                    className="w-30 h-30 radius-50 object-cover"
                  />
                ) : (
                  <div className="w-30 h-30 radius-50 bg-primary text-white d-flex align-center justify-center fs-12 fw-medium">
                    {getInitials(friend.name)}
                  </div>
                )}
                <p className="fs-14 text-color">{friend.name}</p>
              </div>
              <MoreVertical size={16} className="cursor-pointer" />
            </div>
          ))}
        </div>
      ))}
    </div>
  );
};

export default FriendListGroup;
