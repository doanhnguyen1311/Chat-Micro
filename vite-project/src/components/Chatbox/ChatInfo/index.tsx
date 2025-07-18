import avatar from '../../../assets/imgs/avatar-default.jpg';
import { Search, PhoneCall, Info, EllipsisVertical } from 'lucide-react';

const ChatInfo = () => {
    return (
        <div className="d-flex justify-between align-center bg-chat p-24 sticky top-0">
            <div className="d-flex justify-between align-center gap-16px">
                <img src={avatar} alt='ava' className='avatar radius-50'/>
                <div className='d-flex flex-column gap-4px'>
                    <h1 className='fs-16 fw-medium text-color'>Landing Design</h1>
                    <p className='fs-12 fw-medium text-color-secondary'>2 Members</p>
                </div>
            </div>
            <div className="d-flex justify-between align-center gap-28px text-color-secondary cursor-pointer">
                <Search size={20} className='active' />
                <PhoneCall size={20} className='active' />
                <Info size={20} className='active' />
                <EllipsisVertical size={20} className='active' />
            </div>
        </div>
    )
}

export default ChatInfo;