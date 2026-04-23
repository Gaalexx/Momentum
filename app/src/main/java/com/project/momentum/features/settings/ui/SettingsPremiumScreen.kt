package com.project.momentum.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.project.momentum.R
import com.project.momentum.ui.assets.BackCircleButton
import com.project.momentum.ui.assets.BuyButton
import com.project.momentum.ui.assets.PremiumFeatureItem
import com.project.momentum.ui.assets.SubscriptionOptionCard
import com.project.momentum.ui.theme.AppTextStyles
import com.project.momentum.ui.theme.ConstColours

data class SubscriptionOption(
    val title: String,
    val price: String,
    val month_cost: String,
)
@Composable
fun SettingsPremiumScreen(
    onBackClick: () -> Unit,
    onBuyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDuration by remember { mutableStateOf("Месяц") }

    val subscriptionOptions = listOf(
        SubscriptionOption(
            title = stringResource(R.string.settings_premium_one_month),
            price = stringResource(R.string.settings_premium_one_month_cost),
            month_cost = "",
        ),
        SubscriptionOption(
            title = stringResource(R.string.settings_premium_three_month),
            price = stringResource(R.string.settings_premium_three_month_cost),
            month_cost = stringResource(R.string.settings_premium_three_month_cost_per_month),
        ),
        SubscriptionOption(
            title = stringResource(R.string.settings_premium_one_year),
            price = stringResource(R.string.settings_premium_one_year_cost),
            month_cost = stringResource(R.string.settings_premium_one_year_cost_per_month),
        )
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()
            .background(ConstColours.BLACK)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Верхняя панель с заголовком
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            BackCircleButton(
                onClick = onBackClick
            )

            Text(
                text = stringResource(R.string.settings_section_premium),
                style = AppTextStyles.Headlines,
                color = ConstColours.GOLD,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            // Заголовок Premium
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = AppTextStyles.MainText,
                    color = ConstColours.MAIN_BRAND_BLUE,
                    modifier = Modifier.padding(end = 4.dp)
                )

                Text(
                    text = stringResource(R.string.settings_premium_title),
                    style = AppTextStyles.MainText,
                    color = ConstColours.GOLD,
                    modifier = Modifier.padding(end = 4.dp)
                )

                // Описание
                Text(
                    text = stringResource(R.string.settings_premium_description_1),
                    style = AppTextStyles.MainText,
                    color = ConstColours.WHITE
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Описание
                Text(
                    text = stringResource(R.string.settings_premium_description_2),
                    style = AppTextStyles.MainText,
                    color = ConstColours.WHITE,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Список преимуществ
            PremiumFeatureItem(
                text = stringResource(R.string.settings_premium_feature_voice),
                icon = Icons.Default.Mic
            )

            PremiumFeatureItem(
                text = stringResource(R.string.settings_premium_feature_no_limits),
                icon = Icons.Default.AllInclusive
            )

            PremiumFeatureItem(
                text = stringResource(R.string.settings_premium_feature_friends),
                icon = Icons.Default.People
            )

            PremiumFeatureItem(
                text = stringResource(R.string.settings_premium_feature_no_ads),
                icon = Icons.Default.AdsClick
            )

            PremiumFeatureItem(
                text = stringResource(R.string.settings_premium_feature_cloud),
                icon = Icons.Default.Cloud
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Длительность подписки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.settings_premium_duration),
                    style = AppTextStyles.MainText,
                    color = ConstColours.WHITE,
                    modifier = Modifier.padding(bottom = 8.dp)
                )


            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subscriptionOptions.forEach { option ->
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        SubscriptionOptionCard(
                            option = option,
                            isSelected = selectedDuration == option.title,
                            onSelect = { selectedDuration = option.title }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            BuyButton(
                modifier = Modifier,
                onBuyClick
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsPremiumScreenPreview() {
    MaterialTheme {
        SettingsPremiumScreen(
            onBackClick = {},
            onBuyClick = {}
        )
    }
}
