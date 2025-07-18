import { Plus, Search } from "lucide-react";
import FriendListGroup from "./FriendListGroup";

const friendsData = {
    A: [{ id: 1, name: "Alvarez Luna" }],
    C: [{ id: 2, name: "Carla Serrano", avatar: "https://i.pravatar.cc/150?img=2" }],
    D: [
      { id: 3, name: "Dean Vargas" },
      { id: 4, name: "Donaldson Riddle", avatar: "https://i.pravatar.cc/150?img=12" },
      { id: 5, name: "Daniels Webster" },
    ],
    E: [{ id: 6, name: "Earnestine Sears", avatar: "https://i.pravatar.cc/150?img=11" }],
    F: [{ id: 7, name: "Faulkner Benjamin", avatar: "https://i.pravatar.cc/150?img=10" }],
    H: [{ id: 8, name: "Faulkner Benjamin", avatar: "https://i.pravatar.cc/150?img=10" }],
    J: [{ id: 9, name: "Faulkner Benjamin", avatar: "https://i.pravatar.cc/150?img=10" }],
    K: [{ id: 10, name: "Faulkner Benjamin", avatar: "https://i.pravatar.cc/150?img=10" }],
    M: [{ id: 11, name: "Faulkner Benjamin", avatar: "https://i.pravatar.cc/150?img=10" }],
    N: [{ id: 12, name: "Faulkner Benjamin", avatar: "https://i.pravatar.cc/150?img=10" }],
};

const ListFriends = () => {
    return (
        <div className="p-24 pt-0 h-100">
            <div className="sticky top-0 bg-white pt-24">
                <div className="d-flex justify-between align-center mb-24">
                    <h1 className="fs-20 fw-medium">Friends</h1>
                    <div className="d-flex p-8 text-green bg-green cursor-pointer radius-4 btn-green-hover">
                        <Plus size={16}/>
                    </div>
                </div>
                <div className="d-flex justify-between align-center bg-gray radius-4 mb-24">
                    <input type="text" placeholder="Search here..." className="w-100 p-12 fs-14"/>
                    <Search size={16} className="p-12 btn-gray-hover cursor-pointer radius-rigth-4" style={{color: '#495047'}}/>
                </div>
            </div>
            <FriendListGroup data={friendsData} />
        </div>
    )
}

export default ListFriends;