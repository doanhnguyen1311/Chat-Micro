export type Message = {
    content: string;
    time: string;
};

export type ChatGroup = {
    id: number;
    name: string;
    avatar: string;
    lastMessages: Message[];
};
