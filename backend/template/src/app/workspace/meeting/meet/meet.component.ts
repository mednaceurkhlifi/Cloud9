import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { ZegoUIKitPrebuilt } from '@zegocloud/zego-uikit-prebuilt';
import { ActivatedRoute } from '@angular/router';


// get token
function generateToken(tokenServerUrl: string, userID: string) {
    // Obtain the token interface provided by the App Server
    return fetch(
        `${tokenServerUrl}/access_token?userID=${userID}&expired_ts=7200`,
        {
            method: 'GET',
        }
    ).then((res) => res.json());
}

function randomID(len: any) {
    let result = '';
    if (result) return result;
    var chars = '12345qwertyuiopasdfgh67890jklmnbvcxzMNBVCZXASDQWERTYHGFUIOLKJP',
        maxPos = chars.length,
        i;
    len = len || 5;
    for (i = 0; i < len; i++) {
        result += chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return result;
}

@Component({
    selector: 'app-meet',
    standalone: true,
    imports: [],
    templateUrl: './meet.component.html',
    styleUrl: './meet.component.scss'
})
export class MeetComponent implements AfterViewInit{
    @ViewChild('root') root!: ElementRef;

    constructor(
        private route: ActivatedRoute
    ) {
    }

    ngAfterViewInit() {
        const roomID = this.route.snapshot.paramMap.get('room_id') || '1';
        const userID = this.route.snapshot.paramMap.get('user_id') || '1';
        const userName = this.route.snapshot.paramMap.get('username') || 'ouerfelli';
        const appID = '836038057';
        const serverSecret = "0610edd02e373ce6ee9979dbe881d4c6";
        // generate token
        generateToken('https://nextjs-token.vercel.app/api', userID).then((res) => {
            const token = ZegoUIKitPrebuilt.generateKitTokenForProduction(
                1484647939,
                res.token,
                roomID,
                userID,
                userName
            );
            // create instance object from token
            const zp = ZegoUIKitPrebuilt.create(token);

            console.log(
                'this.root.nativeElement',
                this.root.nativeElement.clientWidth
            );
            // start the call
            zp.joinRoom({
                container: this.root.nativeElement,
                sharedLinks: [
                    {
                        name: 'Personal link',
                        url:
                            window.location.origin +
                            window.location.pathname +
                            '?roomID=' +
                            roomID,
                    },
                ],
                scenario: {
                    mode: ZegoUIKitPrebuilt.GroupCall, // To implement 1-on-1 calls, modify the parameter here to [ZegoUIKitPrebuilt.OneONoneCall].
                },
                showPreJoinView: false,
                turnOnMicrophoneWhenJoining: false,
                turnOnCameraWhenJoining: false,
                showMyCameraToggleButton: true,
                showMyMicrophoneToggleButton: true,
                showAudioVideoSettingsButton: true,
                showScreenSharingButton: true,
                showTextChat: true,
                showUserList: true,
                maxUsers: 5,
                layout: "Auto",
                showLayoutButton: false,
            });
        });
    }

}
