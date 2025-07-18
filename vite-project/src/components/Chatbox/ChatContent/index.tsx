import React from "react";
import avatar1 from "../../../assets/imgs/avatar-1.jpg";
import avatar2 from "../../../assets/imgs/avatar-2.jpg";
import avatar3 from "../../../assets/imgs/avatar-3.jpg";
import avatarMe from "../../../assets/imgs/avatar-me.jpg";

interface Message {
    id: number;
    text: string;
    sender: string;
    avatar: string;
    time: string;
    isMe: boolean;
}

const messages: Message[] = [
    {
        id: 1,
        text: "Yeah everything is fine, Our next meeting tomorrow at 10.00 am AM",
        sender: "Carla Serrano",
        avatar: avatar1,
        time: "11:03am",
        isMe: false,
    },
    {
        id: 2,
        text: "Wow that's great",
        sender: "Alvarez Luna",
        avatar: avatar2,
        time: "11:03am",
        isMe: false,
    },
    {
        id: 3,
        text: "Wow that's great",
        sender: "Alvarez Luna",
        avatar: avatar3,
        time: "11:04am",
        isMe: false,
    },
    {
        id: 4,
        text: "Wow that's great",
        sender: "Alvarez Luna",
        avatar: avatarMe,
        time: "11:01am",
        isMe: true,
    },
    {
        id: 5,
        text: "@Jean Berwick, Please Assign AB-123 to me",
        sender: "You",
        avatar: avatarMe,
        time: "11:03am",
        isMe: true,
    },
    {
        id: 6,
        text: "@Jean Berwick, Please Assign AB-123 to me",
        sender: "You",
        avatar: avatarMe,
        time: "11:05am",
        isMe: true,
    },
    {
        id: 7,
        text: "@Jean Berwick, Please Assign AB-123 to me",
        sender: "You",
        avatar: avatarMe,
        time: "11:08am",
        isMe: true,
    },
];

const ChatContent: React.FC = () => {
    return (
        <div
            className="p-24 d-flex flex-column gap-16px justify-end"
            style={{ backgroundColor: '#f2f2f2' }}
        >
            {messages
                .slice()
                .sort((a, b) => {
                    const parseTime = (timeStr: string) => {
                        const [time, modifier] = timeStr.toLowerCase().split(/(am|pm)/);
                        const [hoursStr, minutesStr] = time.trim().split(":");
                        let hours = parseInt(hoursStr, 10);
                        const minutes = parseInt(minutesStr, 10);
                
                        if (modifier === "pm" && hours !== 12) hours += 12;
                        if (modifier === "am" && hours === 12) hours = 0;
                
                        // Trả về tổng số phút từ 00:00
                        return hours * 60 + minutes;
                    };
                
                    return parseTime(a.time) - parseTime(b.time);
                })
                .map((msg) => (
                    <div
                        key={msg.id}
                        className={`d-flex align-end ${msg.isMe ? "justify-end" : "justify-start"}`}
                    >
                        {!msg.isMe && (
                            <img
                                src={msg.avatar}
                                alt={msg.sender}
                                className="w-32 h-32 radius-50 mr-16 mt-auto object-cover"
                            />
                        )}

                        <div
                            className={`d-flex flex-column max-w-60 ${
                            msg.isMe ? "align-end text-right" : "align-start"
                            }`}
                        >
                            <div
                                className={`p-16 radius-4 ${
                                    msg.isMe ? "bg-green" : "bg-white box-shadow"
                                }`}
                            >
                                <span className="text-color fs-14">{msg.text}</span>
                            </div>
                            <div className="d-flex gap-8px align-center mt-12 fs-14">
                                {!msg.isMe && <span className="text-color">{msg.sender}</span>}
                                {msg.isMe && <span className="text-color-secondary fs-12 text-green">✓✓</span>}
                                <span className="text-color-secondary fs-12">{msg.time}</span>
                                {msg.isMe && <span className="text-color">You</span>}
                            </div>
                        </div>

                        {msg.isMe && (
                            <img
                                src={msg.avatar}
                                alt={msg.sender}
                                className="w-32 h-32 radius-50 ml-16 mt-auto object-cover"
                            />
                        )}
                    </div>
                ))
            }
        </div>
    );
};

export default ChatContent;
