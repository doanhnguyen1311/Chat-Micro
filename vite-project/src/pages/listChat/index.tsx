import { Plus, Search } from "lucide-react";

const ListChat = () => {
    return (
        <div className="d-flex flex-column p-24">
            <div className="d-flex justify-between align-center mb-24">
                <h1 className="fs-20 fw-medium">Chats</h1>
                <div className="d-flex p-8 text-green bg-green cursor-pointer radius-4 btn-green-hover">
                    <Plus size={16}/>
                </div>
            </div>
            <div className="d-flex justify-between align-center bg-gray radius-4 mb-24">
                <input type="text" placeholder="Search here..." className="w-100 p-12 fs-14"/>
                <Search size={16} className="p-12 btn-gray-hover cursor-pointer radius-rigth-4" style={{color: '#495047'}}/>
            </div>
            <div className="d-flex flex-column mb-24">
                <div className="d-flex justify-between align-center">
                    <p className="fs-12 text-gray">DIRECT MESSAGES</p>
                    <div className="d-flex p-8 text-green bg-green cursor-pointer radius-4 btn-green-hover">
                        <Plus size={16}/>
                    </div>
                </div>
                <div className="d-flex flex-column">
                    {/*  */}
                </div>
            </div>
            <div className="d-flex flex-column">
                <div className="d-flex justify-between align-center">
                    <p className="fs-12 text-gray">CHANNELS</p>
                    <div className="d-flex p-8 text-green bg-green cursor-pointer radius-4 btn-green-hover">
                        <Plus size={16}/>
                    </div>
                </div>
                <div className="d-flex flex-column">
                    {/*  */}
                </div>
            </div>
        </div>
    )
}

export default ListChat;