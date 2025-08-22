import { Outlet } from "react-router-dom"
import MessengerSidebar from "../components/Messenger"
import Header from "../components/Header"
import MiniSidebar from "../components/MiniSidebar"
import RecentActivity from "../components/Activity"
import GroupHeader from "../components/GroupHeader"
import GroupInfo from "../components/GroupInfo"

const GroupLayout = () => {
    return (
        <div className='main-layout h-100vh'>
            <div className="d-flex h-100">
                <MiniSidebar />
                <div className="bg-white h-100vh w-100 hide-scrollbar" style={{overflowY: 'auto'}}>
                    <Header />
                    <div>
                        <GroupHeader />
                        <div className="d-flex" style={{width: '1140px', margin: 'auto'}}>
                            <GroupInfo />
                            <Outlet />
                            <RecentActivity />
                        </div>
                    </div>
                </div>
                <MessengerSidebar />
            </div>
        </div>
    )
}

export { GroupLayout }